package com.woowacourse.smody.cycle.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProgressRequest {

    private Long cycleId;
    private LocalDateTime progressTime;
    private MultipartFile progressImage;
    private String description;
}
