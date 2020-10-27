package com.qa.choonz.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.qa.choonz.persistence.domain.Artist;
import com.qa.choonz.persistence.repository.ArtistRepository;
import com.qa.choonz.rest.dto.ArtistDTO;


@SpringBootTest
public class ArtistServiceUnitTest {
	

    @Autowired
    private ArtistService service;

    @MockBean
    private ArtistRepository repo;

    @MockBean
    private ModelMapper modelMapper;


    private List<Artist> artists;
    private ArtistDTO artistDTO;
    private Artist testArtistWithId;
    private Artist testArtist;

    final Long id = 1L;
    final String testName = "Lewis Capaldi";
	
	   @BeforeEach
	    void init() {
	        this.artists = new ArrayList<>();
	        this.testArtist = new Artist(testName);
	        this.testArtistWithId = new Artist(testArtist.getName());
	        this.testArtistWithId.setId(id);
	        this.artists.add(testArtist);
	        this.artistDTO = modelMapper.map(testArtistWithId, ArtistDTO.class);
	    }

	    @Test
	    void createTest() {

	        when(this.repo.save(this.testArtist)).thenReturn(this.testArtistWithId);
	        when(this.modelMapper.map(this.testArtistWithId, ArtistDTO.class)).thenReturn(this.artistDTO);

	        ArtistDTO expec = this.artistDTO;
	        ArtistDTO real = this.service.create(this.testArtist);
	        
	        assertThat(expec).isEqualTo(real);

	        verify(this.repo, times(1)).save(this.testArtist);
	    }

	    @Test
	    void readOneTest() {

	        when(this.repo.findById(this.id)).thenReturn(Optional.of(this.testArtistWithId));
	        when(this.modelMapper.map(this.testArtistWithId, ArtistDTO.class)).thenReturn(this.artistDTO);

	        assertThat(this.artistDTO).isEqualTo(this.service.read(this.id));

	        verify(this.repo, times(1)).findById(this.id);
	    }

	    @Test
	    void readAllTest() {

	        when(this.repo.findAll()).thenReturn(this.artists);
	        when(this.modelMapper.map(this.testArtistWithId, ArtistDTO.class)).thenReturn(this.artistDTO);

	        assertThat(this.service.read().isEmpty()).isFalse();

	        verify(this.repo, times(1)).findAll();
	    }

	    @Test
	    void updateTest() {
	    	
	    	Artist art = new Artist("Nortorious BIG");
	    	
	        art.setId(this.id);

	        ArtistDTO artistDTO = new ArtistDTO(id, "Nortorious BIG");

	        Artist updatedArtist = new Artist(artistDTO.getName());
	        
	        updatedArtist.setId(this.id);

	        ArtistDTO updatedAlbumDTO = new ArtistDTO(this.id, updatedArtist.getName());

	        when(this.repo.findById(this.id)).thenReturn(Optional.of(art));
	        when(this.repo.save(art)).thenReturn(updatedArtist);
	        when(this.modelMapper.map(updatedArtist, ArtistDTO.class)).thenReturn(updatedAlbumDTO);

	        assertThat(updatedAlbumDTO).isEqualTo(this.service.update(artistDTO, this.id));

	        verify(this.repo, times(1)).findById(1L);
	        verify(this.repo, times(1)).save(updatedArtist);
	    }

	    @Test
	    void deleteTest() {

	    	when(this.repo.existsById(id)).thenReturn(true, false);
			
			assertThat(this.service.delete(id)).isFalse();
			verify(this.repo, times(1)).deleteById(id);
			verify(this.repo, times(1)).existsById(id);
	    }

}
