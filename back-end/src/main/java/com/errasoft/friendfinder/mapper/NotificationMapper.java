package com.errasoft.friendfinder.mapper;

import com.errasoft.friendfinder.dto.NotificationDto;
import com.errasoft.friendfinder.mapper.security.AccountMapper;
import com.errasoft.friendfinder.model.Notification;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AccountMapper.class})
public interface NotificationMapper {

    NotificationDto toNotificationDto(Notification notification);
    Notification toNotification(NotificationDto notificationDto);
    List<NotificationDto> toNotificationDtoList(List<Notification> notifications);
    List<Notification> toNotificationList(List<NotificationDto> notificationDto);

}
