package com.nguyennd.hogwartsartifactsonline.wizard;

import com.nguyennd.hogwartsartifactsonline.system.exception.NotFoundException;

public class WizardNotFoundException extends NotFoundException {
    public WizardNotFoundException(int id) {
        super("Could not find wizard with Id - "+ id +" :(");
    }
}
