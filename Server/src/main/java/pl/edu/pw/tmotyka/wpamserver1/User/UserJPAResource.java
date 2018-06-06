package pl.edu.pw.tmotyka.wpamserver1.User;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
public class UserJPAResource{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/jpa/users")
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/jpa/users/{id}")
    public User retrieveUser(@PathVariable String id) {
        Optional<User> user = userRepository.findById(id);
        return user.get();
    }

    @GetMapping("/jpa/users/{id}/myposts")
    public List<Post> retrievePostsOfUser(@PathVariable String id) {
        Optional<User> user = userRepository.findById(id);
        return user.get().getMyPosts();
    }

    @GetMapping("/jpa/users/{id}/joinedposts")
    public List<Post> retrieveJoinedPostsOfUser(@PathVariable String id) {
        Optional<User> user = userRepository.findById(id);
        return user.get().getJoinedPosts();
    }

    @PostMapping("/jpa/users/authorize")
    public ResponseEntity<Object> authorize(@Valid @RequestBody LoginForm form){
        Optional<User> u = userRepository.findById(form.getUsername());
        Map<String, String> response = new HashMap<>();

        if(u.get().getPassword().equals(form.getPassword())){
            response.put("success", "true");
            response.put("username", u.get().getUsername());
            response.put("address", u.get().getAddress());
            response.put("contact", u.get().getContact());
        }
        else
        {
            response.put("success", "false");
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/jpa/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        Optional<User> u = userRepository.findById(user.getUsername());

        Map<String, Boolean> response = new HashMap<>();

        if(u.isPresent()){
            response.put("success", false);
        }
        else {
            User savedUser = userRepository.save(user);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getUsername())
                    .toUri();
            response.put("success", true);

            //return ResponseEntity.created(location).build();
        }

        return new ResponseEntity(response, HttpStatus.OK);

    }

    @GetMapping("/jpa/posts")
    public List<Post> retrieveAllPosts() {

        List<Post> posts = postRepository.findAll();
        List<Post> filtered =  posts.stream().filter(post -> post.getOther() == null).collect(Collectors.toList());

        return filtered;
    }


    @GetMapping("/jpa/posts/{id}")
    public Post retrievePost(@PathVariable int id) {
        Optional<Post> post = postRepository.findById(id);

        return post.get();
    }

    @PostMapping("/jpa/users/{id}/posts")
    public ResponseEntity<Object> createPost(@PathVariable String id, @RequestBody Post post) {

        Optional<User> userOptional = userRepository.findById(id);

        User user = userOptional.get();

        post.setAuthor(user);

        postRepository.save(post);

        Map<String, Boolean> response = new HashMap<>();
        response.put("success", true);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(post.getId())
                .toUri();

        //return ResponseEntity.created(location).build();
        return new ResponseEntity(response, HttpStatus.OK);
    }


    @PostMapping("/jpa/posts/{id}")
    public ResponseEntity<Object> joinPost(@PathVariable Integer id, @RequestBody User user) {

        Optional<Post> post = postRepository.findById(id);

        post.get().setOther(user);

        postRepository.save(post.get());

        Map<String, Boolean> response = new HashMap<>();
        response.put("success", true);


        return new ResponseEntity(response, HttpStatus.OK);
    }






}
