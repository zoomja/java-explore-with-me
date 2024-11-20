package dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ResponseDto {
    private String app;
    private String uri;
    private Long hits;
}
