package com.nguyennd.hogwartsartifactsonline.artifact;

import com.nguyennd.hogwartsartifactsonline.system.exception.NotFoundException;

public class ArtifactNotFoundException extends NotFoundException {
    public ArtifactNotFoundException(String id) {
        super("Could not find artifact with Id - "+ id +" :(");
    }
}
