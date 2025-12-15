package com.errasoft.friendfinder.service.bundlemessage;

import com.errasoft.friendfinder.helper.BundleMessage;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class BundleMessageService {

    private ResourceBundleMessageSource messageSource;

    public BundleMessageService(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessageAr(String key){
        try {
            return messageSource.getMessage(key,null,new Locale("ar"));
        }catch (NoSuchMessageException e) {
            return key;
        }

    }

    public String getMessageEn(String key){
        try {
            return messageSource.getMessage(key,null,new Locale("en"));
        }catch (NoSuchMessageException e) {
            return key;
        }

    }

    public BundleMessage  getMessageArEn(String key){

            return new BundleMessage(getMessageAr(key),getMessageEn(key));

           }

    public String getMessage(String key){
        try {
            System.out.println("Locale: " + LocaleContextHolder.getLocale());
            return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
            }catch (NoSuchMessageException e) {
            return key;
        }
    }

    public String getMessage(String key, Object... args) {
        try{
            return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
        }catch (NoSuchMessageException e){
            return key;
        }
    }
}
