package com.nguyennd.hogwartsartifactsonline.wizard;

import com.nguyennd.hogwartsartifactsonline.system.HogwartsDTO;
import com.nguyennd.hogwartsartifactsonline.system.Result;
import com.nguyennd.hogwartsartifactsonline.system.StatusCode;
import com.nguyennd.hogwartsartifactsonline.wizard.converter.WizardDtoToWizardConverter;
import com.nguyennd.hogwartsartifactsonline.wizard.converter.WizardToWizardInfoConverter;
import com.nguyennd.hogwartsartifactsonline.wizard.dto.WizardDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/wizards")
public class WizardController {

    private final WizardService wizardService;

    private final WizardToWizardInfoConverter wizardToWizardInfoConverter;

    private final WizardDtoToWizardConverter wizardDtoToWizardConverter;

    public WizardController(WizardService wizardService, WizardToWizardInfoConverter wizardToWizardInfoConverter, WizardDtoToWizardConverter wizardDtoToWizardConverter) {
        this.wizardService = wizardService;
        this.wizardToWizardInfoConverter = wizardToWizardInfoConverter;
        this.wizardDtoToWizardConverter = wizardDtoToWizardConverter;
    }

    @GetMapping("/{wizardId}")
    public Result findWizardById(@PathVariable int wizardId) {
        Wizard foundWizard = this.wizardService.findById(wizardId);
        HogwartsDTO.WizardInfo wizardDto = this.wizardToWizardInfoConverter.convert(foundWizard);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", wizardDto);
    }

    @GetMapping
    public Result findAllWizards() {
        List<Wizard> foundWizards = this.wizardService.findAll();
        List<HogwartsDTO.WizardInfo> wizardInfos = foundWizards
                .stream()
                .map(this.wizardToWizardInfoConverter::convert)
                .toList();
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Find All Success",
                wizardInfos);
    }

    @PostMapping
    public Result addWizard(@Valid @RequestBody WizardDto wizardDto) {
        Wizard newWizard = this.wizardDtoToWizardConverter.convert(wizardDto);
        Wizard savedWizard = this.wizardService.save(newWizard);
        HogwartsDTO.WizardInfo savedWizardInfo = this.wizardToWizardInfoConverter.convert(savedWizard);
        return new Result(
                true,
                200,
                "Add Success",
                savedWizardInfo
        );
    }

    @PutMapping("/{wizardId}")
    public Result updateWizard(@Valid
                               @RequestBody
                               WizardDto wizardDto,
                               @PathVariable
                               int wizardId
                               ) {
        Wizard update = this.wizardDtoToWizardConverter.convert(wizardDto);
        Wizard updatedWizard = this.wizardService.update(wizardId, update);
        HogwartsDTO.WizardInfo updatedWizardInfo = this.wizardToWizardInfoConverter.convert(updatedWizard);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Update Success",
                updatedWizardInfo
        );
    }

    @DeleteMapping("/{wizardId}")
    public Result deleteWizard(@PathVariable int wizardId) {
        this.wizardService.delete(wizardId);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Delete Success",
                null
        );
    }

    @PutMapping("/{wizardId}/artifacts/{artifactId}")
    public Result assignArtifact(@PathVariable
                                 Integer wizardId,
                                 @PathVariable
                                 String artifactId) {
        this.wizardService.assignArtifact(wizardId, artifactId);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Artifact Assignment Success",
                null
        );
    }

}
