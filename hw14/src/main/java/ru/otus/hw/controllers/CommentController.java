package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.hw.dto.CommentCreateUpdateRequest;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.services.CommentService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    private final BookRepository bookRepository;

    @GetMapping("/{bookId}")
    public String getAllCommentsByBook(@PathVariable("bookId") Long bookId, Model model) {
        List<CommentDto> comments = commentService.findByBookId(bookId);
        model.addAttribute("bookTitle", getBookTitle(bookId));
        model.addAttribute("comments", comments);
        return "/comments/get-all";
    }

    @GetMapping("/create/{bookId}")
    public String getCreateCommentPage(@PathVariable("bookId") Long bookId, Model model) {
        CommentCreateUpdateRequest comment = new CommentCreateUpdateRequest();
        comment.setBookId(bookId);
        model.addAttribute("comment", comment);
        model.addAttribute("bookTitle", getBookTitle(bookId));
        return "/comments/create-form";
    }

    private String getBookTitle(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id '%s' not found".formatted(bookId)))
                .getTitle();
    }

    @PostMapping("/create")
    public String createComment(@ModelAttribute(name = "comment") CommentCreateUpdateRequest request, Model model) {

        commentService.insert(request.getBookId(), request.getText());
        return "redirect:/comments/" + request.getBookId();
    }

    @GetMapping("/edit/{id}")
    public String getUpdateCommentPage(@PathVariable("id") Long id, Model model) {
        CommentDto commentDto = commentService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));

        model.addAttribute("comment", commentDto);
        return "/comments/edit-form";
    }

    @PostMapping("/edit/{id}")
    public String updateComment(@PathVariable("id") Long id,
                                @ModelAttribute("comment") CommentCreateUpdateRequest request) {
        commentService.update(id, request.getText());
        return "redirect:/comments/" + request.getBookId();
    }

    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable("id") Long id) {
        commentService.deleteById(id);
        return "redirect:/books/all";
    }
}