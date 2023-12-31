package br.com.usp.mac0332.snippettool.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.usp.mac0332.snippettool.dto.snippet.SnippetCreateDto;
import br.com.usp.mac0332.snippettool.dto.snippet.SnippetResponseDto;
import br.com.usp.mac0332.snippettool.dto.snippet.SnippetUpdateDto;
import br.com.usp.mac0332.snippettool.dto.tag.TagResponseDto;
import br.com.usp.mac0332.snippettool.service.MyUserDetails;
import br.com.usp.mac0332.snippettool.service.SnippetService;
import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("snippet")
public class SnippetController {

	@Autowired
	private SnippetService service;

	@PostMapping
	public ResponseEntity<SnippetResponseDto> createSnippet(@Valid @RequestBody SnippetCreateDto snippetCreateDto, @AuthenticationPrincipal UserDetails userDetails) {
		SnippetResponseDto response = service.createSnippet(snippetCreateDto, ((MyUserDetails) userDetails).user.id);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/")
	public ResponseEntity<List<SnippetResponseDto>> readSnippets(@RequestParam String filtro, @AuthenticationPrincipal UserDetails userDetails) {
		List<SnippetResponseDto> response = service.readSnippets(filtro, ((MyUserDetails) userDetails).user.id);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{snippetId}")
	public ResponseEntity<SnippetResponseDto> readSnippet(@PathVariable Integer snippetId, @AuthenticationPrincipal UserDetails userDetails) {
		SnippetResponseDto response = service.readSnippet(snippetId, ((MyUserDetails) userDetails).user.id);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{snippetId}/tags")
	public ResponseEntity<List<TagResponseDto>> readTags(@PathVariable Integer snippetId, @AuthenticationPrincipal UserDetails userDetails){
		List<TagResponseDto> response = service.findTags(snippetId, ((MyUserDetails) userDetails).user.id);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{snippetId}")
	public ResponseEntity<SnippetResponseDto> updateSnippet(@PathVariable Integer snippetId, @RequestBody SnippetUpdateDto updatedSnippet, @AuthenticationPrincipal UserDetails userDetails) {
		SnippetResponseDto response = service.updateSnippet(snippetId, updatedSnippet,
				((MyUserDetails) userDetails).user.id);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{snippetId}")
	public ResponseEntity<Void> deleteSnippet(@PathVariable Integer snippetId, @AuthenticationPrincipal UserDetails userDetails) {
		service.deleteSnippet(snippetId, ((MyUserDetails) userDetails).user.id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{snippetId}/addTag/{tagId}")
	public ResponseEntity<SnippetResponseDto> addTagToSnippet(@PathVariable Integer snippetId, @PathVariable Integer tagId, @AuthenticationPrincipal UserDetails userDetails) {
		SnippetResponseDto response = service.addTagToSnippet(snippetId, tagId, ((MyUserDetails) userDetails).user.id);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{snippetId}/removeTag/{tagId}")
	public ResponseEntity<SnippetResponseDto> removeTagFromSnippet(@PathVariable Integer snippetId, @PathVariable Integer tagId, @AuthenticationPrincipal UserDetails userDetails) {
		SnippetResponseDto response = service.removeTagFromSnippet(snippetId, tagId, ((MyUserDetails) userDetails).user.id);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/folder/{folderId}/")
	public ResponseEntity<List<SnippetResponseDto>> getSnippetsByFolder(@PathVariable Integer folderId, @RequestParam String filtro, @AuthenticationPrincipal UserDetails userDetails){
		List<SnippetResponseDto> response = service.findByFolderAndFiltro(folderId, filtro, ((MyUserDetails) userDetails).user.id);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/tag/{tagId}/")
	public ResponseEntity<List<SnippetResponseDto>> getSnippetsByTag(@PathVariable Integer tagId, @RequestParam String filtro, @AuthenticationPrincipal UserDetails userDetails){
		List<SnippetResponseDto> response = service.findByTagAndFiltro(tagId, filtro, ((MyUserDetails) userDetails).user.id);
		return ResponseEntity.ok(response);
	}
}
