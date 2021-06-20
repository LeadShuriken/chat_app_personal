package com.chat.controllers;

import com.chat.annotations.SQLInjectionSafe;
import com.chat.dao.Message;
import com.chat.repository.TokenDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.UUID;

import javax.validation.Valid;

@RestController
@RequestMapping("${api.version}")
public class MessageController {

    @Autowired
    private TokenDataService tokenDataService;

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/join")
    public ModelAndView join(@RequestParam @SQLInjectionSafe String id, ModelMap model) {
        if (!tokenDataService.tokenExists(id)) {
            RedirectView v = new RedirectView("/index");
            v.setExposePathVariables(false);
            v.setExposeModelAttributes(false);
            return new ModelAndView(v);
        }
        model.addAttribute("chatname", id);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                "user-" + UUID.randomUUID(), null, Collections.singleton((GrantedAuthority) () -> "USER"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ModelAndView("forward:/index", model);
    }

    @MessageMapping("/register/{chatName}")
    @SendTo("/chat/{chatName}")
    public Message register(@Valid @Payload Message chatMessage, SimpMessageHeaderAccessor headerAccessor,
            @DestinationVariable @SQLInjectionSafe String chatName) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("usertype", authentication.getName());
        headerAccessor.getSessionAttributes().put("chatname", chatName);
        return chatMessage;
    }

    @MessageMapping("/send/{chatName}")
    public void sendMessage(@Valid @Payload Message chatMessage, StompHeaderAccessor headers,
            @DestinationVariable @SQLInjectionSafe String chatName) {
        switch (chatMessage.getType()) {
            case VD_OFFERING:
            case VD_ANSWER:
            case VD_CANCEL:
                Optional<String> user = Optional.ofNullable(headers.getUser()).map(Principal::getName);
                if (user.isPresent()) {
                    List<String> subscribers = simpUserRegistry.getUsers().stream().map(SimpUser::getName)
                            .filter(name -> !user.get().equals(name)).collect(Collectors.toList());
                    subscribers.forEach(sub -> simpMessagingTemplate.convertAndSendToUser(sub, "/queue", chatMessage));
                }
                break;
            default:
                simpMessagingTemplate.convertAndSend("/chat/" + chatName, chatMessage);
                break;
        }
    }
}
