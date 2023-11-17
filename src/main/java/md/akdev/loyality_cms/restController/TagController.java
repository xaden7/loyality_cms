package md.akdev.loyality_cms.restController;

import md.akdev.loyality_cms.dto.TagDTO;
import md.akdev.loyality_cms.repository.TagRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/tags")
public class TagController {
    private final TagRepository tagRepository;

    private final org.modelmapper.ModelMapper modelMapper;

    @Autowired
    public TagController(TagRepository tagRepository, ModelMapper modelMapper) {
        this.tagRepository = tagRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllTags(){
        return ResponseEntity.ok().body(
                tagRepository.findAll().stream().map((element) ->
                        modelMapper.map(element, TagDTO.class)).collect(Collectors.toList()));
    }
}
