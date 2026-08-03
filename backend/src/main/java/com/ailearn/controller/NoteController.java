package com.ailearn.controller;

import com.ailearn.common.PageParam;
import com.ailearn.common.PageResult;
import com.ailearn.common.Result;
import com.ailearn.entity.Note;
import com.ailearn.service.NoteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/mine")
    public Result<PageResult<Note>> mine(PageParam pageParam,
                                         @RequestParam(required = false) Long courseId) {
        return Result.success(noteService.myNotes(pageParam, courseId));
    }

    @GetMapping("/public")
    public Result<List<Note>> publicNotes(@RequestParam(required = false) Long courseId) {
        return Result.success(noteService.publicNotes(courseId));
    }

    @PostMapping
    public Result<Note> create(@RequestParam(required = false) Long courseId,
                               @RequestParam(required = false) Long lessonId,
                               @RequestParam String title,
                               @RequestParam(required = false) String content,
                               @RequestParam(required = false) Boolean isPublic) {
        return Result.success(noteService.create(courseId, lessonId, title, content, isPublic));
    }

    @PutMapping("/{id}")
    public Result<Note> update(@PathVariable Long id,
                               @RequestParam(required = false) String title,
                               @RequestParam(required = false) String content,
                               @RequestParam(required = false) Boolean isPublic) {
        return Result.success(noteService.update(id, title, content, isPublic));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        noteService.delete(id);
        return Result.success();
    }
}
