package com.nguyennd.hogwartsartifactsonline.wizard;

import com.nguyennd.hogwartsartifactsonline.artifact.Artifact;
import com.nguyennd.hogwartsartifactsonline.artifact.ArtifactRepository;
import com.nguyennd.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WizardService {

    private final WizardRepository wizardRepository;

    private final ArtifactRepository artifactRepository;

    public WizardService(WizardRepository wizardRepository, ArtifactRepository artifactRepository) {
        this.wizardRepository = wizardRepository;
        this.artifactRepository = artifactRepository;
    }

    public Wizard findById(int wizardId) {
        return wizardRepository.findById(wizardId).orElseThrow(() ->
                new ObjectNotFoundException(Wizard.class.getSimpleName(),wizardId)
        );
    }

    public List<Wizard> findAll() {
        return wizardRepository.findAll();
    }

    public Wizard save(Wizard newWizard) {
        return this.wizardRepository.save(newWizard);
    }

    public Wizard update(int wizardId, Wizard update) {
        return wizardRepository.findById(wizardId).map(
                oldWizard -> {
                    oldWizard.setName(update.getName());
                    return wizardRepository.save(oldWizard);
                }
        ).orElseThrow(() -> new ObjectNotFoundException(Wizard.class.getSimpleName(),wizardId));
    }

    public void delete(int wizardId) {
        Wizard wizard = this.wizardRepository.findById(wizardId)
                .orElseThrow(() -> new ObjectNotFoundException(Wizard.class.getSimpleName(),wizardId)
                );
        wizard.removeArtifacts();
        this.wizardRepository.deleteById(wizardId);
    }

    public void assignArtifact(Integer wizardId, String artifactId) {
        // find artifact
        Artifact artifactTobeAssigned = this.artifactRepository.findById(artifactId).orElseThrow(() ->
                new ObjectNotFoundException(Artifact.class.getSimpleName(), artifactId)
        );

        // find wizard
        Wizard wizard = this.wizardRepository.findById(wizardId).orElseThrow(() ->
                new ObjectNotFoundException(Wizard.class.getSimpleName(), wizardId));

        // artifact asssignment
        // if the artifact is already owned by some wizard
        if (artifactTobeAssigned.getOwner() != null) {
            artifactTobeAssigned.getOwner().removeArtifact(artifactTobeAssigned);
        }
        wizard.addArtifact(artifactTobeAssigned);
    }
}
