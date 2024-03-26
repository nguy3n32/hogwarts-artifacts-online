package com.nguyennd.hogwartsartifactsonline.wizard;

import com.nguyennd.hogwartsartifactsonline.artifact.Artifact;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WizardServiceTest {

    @Mock
    WizardRepository wizardRepository;

    @InjectMocks
    WizardService wizardService;

    List<Wizard> wizards;

    @BeforeEach
    void setUp() {
        this.wizards = new ArrayList<>();
        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");
        this.wizards.add(w1);

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");
        this.wizards.add(w2);

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Neville Longbottom");
        this.wizards.add(w3);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void testFindByIdSuccess() {
        // Given
        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");

        Artifact a = new Artifact();
        a.setId("1250808601744904193");
        a.setName("Elder Wand");
        a.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
        a.setImageUrl("ImageUrl");

        w1.addArtifact(a);

        given(wizardRepository.findById(1)).willReturn(Optional.of(w1));

        // When
        Wizard returnedWizard = wizardService.findById(1);

        // Then
        assertThat(returnedWizard.getId()).isEqualTo(w1.getId());
        assertThat(returnedWizard.getName()).isEqualTo(w1.getName());

        verify(wizardRepository, times(1)).findById(1);
    }

    @Test
    void testFindByIdNotfound() {
        // Given
        given(wizardRepository.findById(Mockito.any(Integer.class)))
                .willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(() -> {
            Wizard returnedWizard = wizardService.findById(2);
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(WizardNotFoundException.class)
                .hasMessage("Could not find wizard with Id - 2 :(");
        verify(wizardRepository, times(1)).findById(2);

    }

    @Test
    void testFindAllSuccess() {
        // Given
        given(wizardRepository.findAll()).willReturn(this.wizards);

        // When
        List<Wizard> actualWizard = wizardService.findAll();

        // Then
        assertThat(actualWizard).hasSameSizeAs(this.wizards);
        verify(wizardRepository, times(1)).findAll();
    }

    @Test
    void testSaveWizardSuccess() {
        // Given
        Wizard newWizard = new Wizard();
        newWizard.setId(4);
        newWizard.setName("Albus Dumbledore");

        given(wizardRepository.save(newWizard)).willReturn(newWizard);

        // When
        Wizard savedWizard = wizardService.save(newWizard);

        // Then
        assertThat(savedWizard.getId()).isEqualTo(4);
        assertThat(savedWizard.getName()).isEqualTo(newWizard.getName());
        assertThat(savedWizard.getNumberOfArtifacts()).isEqualTo(newWizard.getNumberOfArtifacts());

    }

    @Test
    void testUpdateSuccess() {
        // Given
        Wizard oldWizard = new Wizard();
        oldWizard.setId(2);
        oldWizard.setName("Harry Potter");

        Wizard update = new Wizard();
        update.setId(2);
        update.setName("Harry Potter fake");

        given(wizardRepository.findById(2)).willReturn(Optional.of(oldWizard));
        given(wizardRepository.save(oldWizard)).willReturn(oldWizard);

        // When
        Wizard updatedWizard = wizardService.update(2, update);

        // Then
        assertThat(updatedWizard.getId()).isEqualTo(update.getId());
        assertThat(updatedWizard.getName()).isEqualTo(update.getName());
        verify(wizardRepository, times(1)).findById(2);
        verify(wizardRepository, times(1)).save(oldWizard);
    }

    @Test
    void testUpdateWizardNotFound() {
        // Given
        Wizard update = new Wizard();
        update.setId(2);
        update.setName("Harry Potter fake");

        given(wizardRepository.findById(2)).willReturn(Optional.empty());

        // When
        assertThrows(WizardNotFoundException.class, () ->{
            wizardService.update(2, update);
        });

        // Then
        verify(wizardRepository, times(1)).findById(2);
    }

    @Test
    void testDeleteSuccess() {
        // Given
        Wizard wizard = new Wizard();
        wizard.setId(2);
        wizard.setName("Harry Potter fake");

        given(wizardRepository.findById(2)).willReturn(Optional.of(wizard));
        doNothing().when(wizardRepository).deleteById(2);

        // When
        wizardService.delete(2);

        // Then
        verify(wizardRepository, times(1)).deleteById(2);
    }

    @Test
    void testDeleteNotFound() {
        // Given
        given(wizardRepository.findById(2)).willReturn(Optional.empty());

        // When
        assertThrows(WizardNotFoundException.class, () -> {
            wizardService.delete(2);
        });

        // Then
        verify(wizardRepository, times(1)).findById(2);
    }










}
