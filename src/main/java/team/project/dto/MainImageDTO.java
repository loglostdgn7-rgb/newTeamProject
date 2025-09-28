package team.project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MainImageDTO {
    private int id;
    private int productId;
    private byte[] imageData;
    private String base64Image;

}