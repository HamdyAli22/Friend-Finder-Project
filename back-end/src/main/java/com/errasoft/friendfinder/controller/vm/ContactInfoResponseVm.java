package com.errasoft.friendfinder.controller.vm;

import com.errasoft.friendfinder.dto.ContactInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ContactInfoResponseVm {
    private List<ContactInfoDto> messages;

    private Long totalMessages;

}
