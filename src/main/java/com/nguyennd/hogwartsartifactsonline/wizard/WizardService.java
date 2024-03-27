package com.nguyennd.hogwartsartifactsonline.wizard;

import com.nguyennd.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WizardService {

    private final WizardRepository wizardRepository;

    public WizardService(WizardRepository wizardRepository) {
        this.wizardRepository = wizardRepository;
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
}
