package org.example.book.springboot.service.posts;

import lombok.RequiredArgsConstructor;
import org.example.book.springboot.domain.posts.Posts;
import org.example.book.springboot.domain.posts.PostsRepository;
import org.example.book.springboot.web.dto.PostsResponseDto;
import org.example.book.springboot.web.dto.PostsSaveRequestDto;
import org.example.book.springboot.web.dto.PostsUpdateRequestDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    //JPA 영속성 컨텍스트가 유지된 상태에 Entity객체의 값을 변경하면, 트랜잭션이 끝나는 시점에 해당 테이블에 변경분을 반영.
    //따라서 해당 서비스 메소드에 @Transactional 어노테이션 필수!
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto){
        Posts posts = postsRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        return new PostsResponseDto(entity);
    }
}
