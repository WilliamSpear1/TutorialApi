package com.example.demo;

import com.example.demo.model.Tutorial;
import com.example.demo.repository.TutorialRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class JPAUnitTest {
	@Autowired
	private TestEntityManager testEntityManager;
	
	@Autowired
	TutorialRepository tutorialRepository;
	
	@Test
	public void should_find_no_tutorials_if_repository_is_empty() {
		Iterable tutorials = tutorialRepository.findAll();
		
		assertThat(tutorials).isEmpty();
	}
	
	@Test
	public void should_store_a_tutorial() {
		Tutorial tutorial = tutorialRepository.save(
			new Tutorial("Good Code, Bad Code", "Think like a software engineer", true)
		);
		
		assertThat(tutorial).hasFieldOrPropertyWithValue("title", "Good Code, Bad Code");
		assertThat(tutorial).hasFieldOrPropertyWithValue("description", "Think like a software engineer");
		assertThat(tutorial).hasFieldOrPropertyWithValue("published", true);
	}
	
	@Test
	public void should_find_all_tutorials() {
		Tutorial tutorial1 = new Tutorial("Title1", "Desc1", true);
		testEntityManager.persist(tutorial1);
		
		Tutorial tutorial2 = new Tutorial("Title2", "Desc2", false);
		testEntityManager.persist(tutorial2);
		
		Tutorial tutorial3 = new Tutorial("Title3", "Desc3", true);
		testEntityManager.persist(tutorial3);
		
		Iterable tutorials = tutorialRepository.findAll();
		
		assertThat(tutorials).hasSize(3).contains(tutorial1, tutorial2, tutorial3);
	}
	
	@Test
	public void should_find_tutorial_by_id() {
		Tutorial tutorial1 = new Tutorial("Title1", "Desc1", true);
		testEntityManager.persist(tutorial1);
		
		Tutorial tutorial2 = new Tutorial("Title2", "Desc2", false);
		testEntityManager.persist(tutorial2);
		
		Tutorial foundTutorials = tutorialRepository.findById(tutorial2.getId()).get();
		
		assertThat(foundTutorials).isEqualTo(tutorial2);
	}
	
	@Test
	public void should_find_published_tutorials() {
		Tutorial tutorial1 = new Tutorial("Title1", "Desc1", true);
		testEntityManager.persist(tutorial1);
		
		Tutorial tutorial2 = new Tutorial("Title2", "Desc2", false);
		testEntityManager.persist(tutorial2);
		
		Tutorial tutorial3 = new Tutorial("Title3", "Desc3", true);
		testEntityManager.persist(tutorial3);
		
		Iterable tutorials = tutorialRepository.findByPublished(true);
		
		assertThat(tutorials).hasSize(2).contains(tutorial1, tutorial3);
	}
	
	@Test
	public void should_find_tutorials_by_title_containing_string() {
		Tutorial tutorial1 = new Tutorial("Dinner Date", "Desc1", true);
		testEntityManager.persist(tutorial1);
		
		Tutorial tutorial2 = new Tutorial("Save the Date", "Desc2", false);
		testEntityManager.persist(tutorial2);
		
		Tutorial tutorial3 = new Tutorial("Fighters", "Desc3", true);
		testEntityManager.persist(tutorial3);
		
		Iterable tutorials = tutorialRepository.findByTitleContaining("ate");
		
		assertThat(tutorials).hasSize(2).contains(tutorial1, tutorial2);
	}
	
	@Test
	public void should_update_tutorial_by_Id_() {
		Tutorial tutorial1 = new Tutorial("Dinner Date", "Desc1", true);
		testEntityManager.persist(tutorial1);
		
		Tutorial tutorial2 = new Tutorial("Save the Date", "Desc2", false);
		testEntityManager.persist(tutorial2);
		
		Tutorial updatedTutorial = new Tutorial("updated tutorial2", "updated desc2", true);
	
		Tutorial tutorial = tutorialRepository.findById(tutorial2.getId()).get();
		
		tutorial.setTitle(updatedTutorial.getTitle());
		tutorial.setDescription(updatedTutorial.getDescription());
		tutorial.setPublished(updatedTutorial.isPublished());
		tutorialRepository.save(tutorial);
		
		Tutorial checkTutorial = tutorialRepository.findById(tutorial2.getId()).get();
		
		assertThat(checkTutorial.getId()).isEqualTo(tutorial2.getId());
		assertThat(checkTutorial.getTitle()).isEqualTo(tutorial2.getTitle());
		assertThat(checkTutorial.getDescription()).isEqualTo(tutorial2.getDescription());
		assertThat(checkTutorial.isPublished()).isEqualTo(tutorial2.isPublished());
	}
	
	@Test
	public void should_delete_tutorial_by_Id_() {
		Tutorial tutorial1 = new Tutorial("Dinner Date", "Desc1", true);
		testEntityManager.persist(tutorial1);
		
		Tutorial tutorial2 = new Tutorial("Save the Date", "Desc2", false);
		testEntityManager.persist(tutorial2);
		
		Tutorial tutorial3 = new Tutorial("Fighters", "Desc3", true);
		testEntityManager.persist(tutorial3);
		
		tutorialRepository.deleteById(tutorial2.getId());
		
		Iterable tutorials = tutorialRepository.findAll();
		
		assertThat(tutorials).hasSize(2).contains(tutorial1, tutorial3);
	}
	
	@Test
	public void should_delete_all_tutorials_() {
		testEntityManager.persist(new Tutorial("Tutorial", "Description", true));
		testEntityManager.persist(new Tutorial("Tutorial2", "Description2", false));
		
		tutorialRepository.deleteAll();
		
		assertThat(tutorialRepository.findAll()).isEmpty();
	}
}