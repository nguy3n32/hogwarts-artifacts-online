package com.nguyennd.hogwartsartifactsonline.artifact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //Optional
public interface ArtifactRepository extends JpaRepository<Artifact, String> {
}
