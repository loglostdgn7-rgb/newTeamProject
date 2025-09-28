package team.project.mapper;

import org.apache.ibatis.annotations.Mapper;
import team.project.dto.MainImageDTO;

@Mapper
public interface MainImageMapper {
    MainImageDTO selectRandomMainImage();
}
