package com.nguyennd.hogwartsartifactsonline.artifact.converter;

import com.nguyennd.hogwartsartifactsonline.artifact.Artifact;
import com.nguyennd.hogwartsartifactsonline.artifact.dto.ArtifactDto;
import com.nguyennd.hogwartsartifactsonline.wizard.WizardToWizardDtoConverter;
import com.nguyennd.hogwartsartifactsonline.wizard.dto.WizardDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactToArtifactDtoConverter implements Converter<Artifact, ArtifactDto> {
    private final WizardToWizardDtoConverter wizardToWizardDtoConverter;

    public ArtifactToArtifactDtoConverter(WizardToWizardDtoConverter wizardToWizardDtoConverter) {
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
    }

    @Override
    public ArtifactDto convert(Artifact source) {
        return new ArtifactDto(
                source.getId(),
                source.getName(),
                source.getDescription(),
                source.getImageUrl(),
                source.getOwner() != null ?
                        this.wizardToWizardDtoConverter.convert(source.getOwner())
                        : null
        );
    }
}
