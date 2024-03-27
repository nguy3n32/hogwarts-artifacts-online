package com.nguyennd.hogwartsartifactsonline.wizard.converter;

import com.nguyennd.hogwartsartifactsonline.wizard.Wizard;
import com.nguyennd.hogwartsartifactsonline.wizard.dto.WizardDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizardDtoToWizardConverter implements Converter<WizardDto, Wizard> {
    @Override
    public Wizard convert(WizardDto source) {
        Wizard wizard = new Wizard();
        wizard.setName(source.name());
        return wizard;
    }
}
