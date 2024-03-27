package com.nguyennd.hogwartsartifactsonline.wizard.converter;

import com.nguyennd.hogwartsartifactsonline.artifact.converter.ArtifactToArtifactInfoConverter;
import com.nguyennd.hogwartsartifactsonline.system.HogwartsDTO;
import com.nguyennd.hogwartsartifactsonline.wizard.Wizard;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WizardToWizardInfoConverter implements Converter<Wizard, HogwartsDTO.WizardInfo> {

    private final ArtifactToArtifactInfoConverter artifactToArtifactInfoConverter;

    public WizardToWizardInfoConverter(ArtifactToArtifactInfoConverter artifactToArtifactInfoConverter) {
        this.artifactToArtifactInfoConverter = artifactToArtifactInfoConverter;
    }

    @Override
    public HogwartsDTO.WizardInfo convert(Wizard source) {
        List<HogwartsDTO.ArtifactInfo> artifacts = source.getArtifacts() != null ?
                source.getArtifacts().stream().map(
                        this.artifactToArtifactInfoConverter::convert
                ).collect(Collectors.toList()) : null;
        return new HogwartsDTO.WizardInfo(
                source.getId(),
                source.getName(),
                source.getNumberOfArtifacts(),
                artifacts
        );
    }
}
