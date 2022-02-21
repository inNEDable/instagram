package com.example.instagramproject.controler;
import com.example.instagramproject.model.DTO.ReturnTagDTO;
import com.example.instagramproject.model.entity.PostEntity;
import com.example.instagramproject.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.TreeSet;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @PostMapping("add/{postId}/{tagText}")
    public ResponseEntity<ReturnTagDTO> addTagToPost (@PathVariable Long postId, @PathVariable String tagText, HttpServletRequest request){
        ReturnTagDTO returnTagDTO = tagService.addTagToPost(postId, tagText, request);

        return new ResponseEntity<>(returnTagDTO, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("delete/{postId}/{tagId}")
    public void removeTagFromPost (@PathVariable Long postId, @PathVariable Long tagId, HttpServletRequest request){
        tagService.deleteTagFromPost(postId, tagId, request);
    }

    @GetMapping("/get-all-posts-by/{tagText}")
    public ResponseEntity<TreeSet<PostEntity>> getAllPostsByTag (@PathVariable String tagText, HttpServletRequest request){
        TreeSet<PostEntity> postEntities = tagService.getAllPostsByTag(tagText, request);
        return new ResponseEntity<>(postEntities, HttpStatus.OK);
    }

    @GetMapping("/get-all-tags-from-post/{postId}")
    ResponseEntity<Set<ReturnTagDTO>> getAllTagsFromPost (@PathVariable Long postId, HttpServletRequest request){
        Set<ReturnTagDTO> returnTagDTOS = tagService.getAllTagsFromPost(postId, request);
        return new ResponseEntity<>(returnTagDTOS, HttpStatus.OK);
    }
}
