package com.example.instagramproject.service;

import com.example.instagramproject.exceptions.InvalidDataException;
import com.example.instagramproject.exceptions.UnauthorizedAccessException;
import com.example.instagramproject.model.DTO.ReturnPostDTO;
import com.example.instagramproject.model.DTO.ReturnTagDTO;
import com.example.instagramproject.model.entity.PostEntity;
import com.example.instagramproject.model.entity.TagEntity;
import com.example.instagramproject.model.repository.PostRepository;
import com.example.instagramproject.model.repository.TagRepository;
import com.example.instagramproject.util.SessionManager;
import com.example.instagramproject.util.Validator;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Service
public class TagService {

    public static final int MAX_TAG_TEXT = 100;
    public static final int MIN_TAG_TEXT = 1;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private ModelMapper modelMapper;


    public ReturnTagDTO addTagToPost(Long postId, String tagText, HttpServletRequest request) {
        Validator.nullChecker(postId, tagText);
        Validator.validateStringLength(MIN_TAG_TEXT, MAX_TAG_TEXT, tagText);

        PostEntity postEntity = tagToPostValidation(postId, request);

        TagEntity tagEntity = tagRepository.findByText(tagText);
        if (tagEntity == null) {
            tagEntity = generateNewTagAndAddToPost(postEntity, tagText);
        } else {
            if (postEntity.getTags().contains(tagEntity))
                throw new InvalidDataException("This tag is already assigned to the post");
            tagEntity.getPosts().add(postEntity);
            tagRepository.save(tagEntity);
        }

        return modelMapper.map(tagEntity, ReturnTagDTO.class);
    }

    public void deleteTagFromPost(Long postId, Long tagId, HttpServletRequest request) {
        Validator.nullChecker(postId, tagId);
        PostEntity postEntity = tagToPostValidation(postId, request);
        TagEntity tagEntity = Validator.getEntity(tagId, tagRepository);
        if (tagEntity.getPosts() == null || !tagEntity.getPosts().contains(postEntity))
            throw new InvalidDataException("This post doesn't have the specified tag!");

        tagEntity.getPosts().remove(postEntity);
        tagRepository.save(tagEntity);
    }

    public TreeSet<PostEntity> getAllPostsByTag(String tagText, HttpServletRequest request) {
        Validator.nullChecker(tagText);
        Validator.validateStringLength(MIN_TAG_TEXT, MAX_TAG_TEXT, tagText);
        sessionManager.authorizeSession(null, request.getSession(), request);

        TagEntity tagEntity = tagRepository.findByText(tagText);
        if (tagEntity == null) throw new InvalidDataException("Such tag doesn't exist");

        Set<PostEntity> postEntities = tagEntity.getPosts();
        if (postEntities.isEmpty()) throw new InvalidDataException("This tag has no posts");

        return modelMapper.map(postEntities, new TypeToken<TreeSet<ReturnPostDTO>>() {
        }.getType());
    }

    public Set<ReturnTagDTO> getAllTagsFromPost(Long postId, HttpServletRequest request) {
        Validator.nullChecker(postId);
        sessionManager.authorizeSession(null, request.getSession(), request);
        PostEntity postEntity = Validator.getEntity(postId, postRepository);

        Set<TagEntity> tagEntities = postEntity.getTags();
        if (tagEntities.isEmpty()) throw new InvalidDataException("This post doesn't have any tags");

        return modelMapper.map(tagEntities, new TypeToken<Set<ReturnTagDTO>>() {
        }.getType());
    }

    private PostEntity tagToPostValidation(Long postId, HttpServletRequest request) {
        sessionManager.authorizeSession(null, request.getSession(), request);
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() -> new InvalidDataException("Post doesn't exist"));
        if (postEntity.getUser().getId() != sessionManager.getUserID(request))
            throw new UnauthorizedAccessException("This post doesn't belong to the user!");
        return postEntity;
    }

    private TagEntity generateNewTagAndAddToPost(PostEntity postEntity, String tagText) {
        TagEntity newTagEntity = new TagEntity();
        newTagEntity.setText(tagText);
        newTagEntity.setPosts(new HashSet<>());
        newTagEntity.getPosts().add(postEntity);

        tagRepository.save(newTagEntity);

        return newTagEntity;
    }
}
