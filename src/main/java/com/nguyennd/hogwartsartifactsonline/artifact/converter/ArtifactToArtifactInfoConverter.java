package com.nguyennd.hogwartsartifactsonline.artifact.converter;

import com.nguyennd.hogwartsartifactsonline.artifact.Artifact;
import com.nguyennd.hogwartsartifactsonline.system.HogwartsDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactToArtifactInfoConverter implements Converter<Artifact, HogwartsDTO.ArtifactInfo> {
    @Override
    public HogwartsDTO.ArtifactInfo convert(Artifact source) {
        return new HogwartsDTO.ArtifactInfo(
                source.getId(),
                source.getName(),
                source.getDescription(),
                source.getImageUrl()
        );
    }
}
