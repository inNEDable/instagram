package com.example.instagramproject.service;

import com.example.instagramproject.exceptions.InvalidData;
import com.example.instagramproject.exceptions.UnauthorizedAccess;
import com.example.instagramproject.model.DTO.ReturnTagDTO;
import com.example.instagramproject.model.entity.PostEntity;
import com.example.instagramproject.model.entity.TagEntity;
import com.example.instagramproject.model.repository.PostRepository;
import com.example.instagramproject.model.repository.TagRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;

@Service
public class TagService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private ModelMapper modelMapper;


    public ReturnTagDTO addTagToPost(Long postId, String tagText, HttpServletRequest request) {
        if (postId == null || tagText.isBlank()) throw new InvalidData("Missing data in request!");
        PostEntity postEntity = tagToPostValidation(postId, request);

        TagEntity tagEntity = tagRepository.findByText(tagText);
        if (tagEntity == null){
            tagEntity = generateNewTagAndAddToPost(postEntity, tagText);
        } else {
            if (postEntity.getTags().contains(tagEntity)) throw new InvalidData("This tag is already assigned to the post");
            tagEntity.getPosts().add(postEntity);
            tagRepository.save(tagEntity);
        }

        return modelMapper.map(tagEntity, ReturnTagDTO.class);

    }

    public void deleteTagFromPost(Long postId, Long tagId, HttpServletRequest request) {
        if (postId == null || tagId == null) throw new InvalidData("Missing data in request!");
        PostEntity postEntity = tagToPostValidation(postId, request);
        TagEntity tagEntity = tagRepository.findById(tagId).orElseThrow(() -> new InvalidData("Such tag doesn't exist!"));

        if (tagEntity.getPosts() == null || !tagEntity.getPosts().contains(postEntity))
            throw new InvalidData("This post doesn't have the specified tag!");

        tagEntity.getPosts().remove(postEntity);
        tagRepository.save(tagEntity);

    }

    private TagEntity generateNewTagAndAddToPost(PostEntity postEntity, String tagText) {
        TagEntity newTagEntity = new TagEntity();
        newTagEntity.setText(tagText);
        newTagEntity.setPosts(new HashSet<>());
        newTagEntity.getPosts().add(postEntity);

        tagRepository.save(newTagEntity);

        return newTagEntity;
    }

    private PostEntity tagToPostValidation(Long postId, HttpServletRequest request) {
        sessionManager.authorizeSession(null, request.getSession(), request);
        PostEntity postEntity =  postRepository.findById(postId).orElseThrow(() -> new InvalidData("Post doesn't exist"));
        if (postEntity.getUser().getId() != sessionManager.getUserID(request))
            throw new UnauthorizedAccess("This post doesn't belong to the user!");
        return postEntity;
    }
}
