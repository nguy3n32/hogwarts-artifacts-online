package com.nguyennd.hogwartsartifactsonline.artifact.dto;

import com.nguyennd.hogwartsartifactsonline.wizard.dto.WizardDto;
import jakarta.validation.constraints.NotEmpty;

public record ArtifactDto(String id,
                          @NotEmpty(message = "name is required.")
//                                  @Length(min = 0, max = 255)
//                                  @Pattern("regex")
                          String name,
                          @NotEmpty(message = "description is required.")
                          String description,
                          @NotEmpty(message = "imageUrl is required.")
                          String imageUrl,
                          WizardDto owner) {
}
