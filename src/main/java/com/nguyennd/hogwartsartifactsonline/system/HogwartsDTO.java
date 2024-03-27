package com.nguyennd.hogwartsartifactsonline.system;

import com.nguyennd.hogwartsartifactsonline.artifact.Artifact;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public interface HogwartsDTO {
    record WizardInfo(Integer id,
                      String name,
                      Integer numberOfArtifact,
                      List<ArtifactInfo> artifacts){}

    record ArtifactInfo(String id,
                        @NotEmpty(message = "name is required.")
//                                  @Length(min = 0, max = 255)
//                                  @Pattern("regex")
                        String name,
                        @NotEmpty(message = "description is required.")
                        String description,
                        @NotEmpty(message = "imageUrl is required.")
                        String imageUrl){}
}
