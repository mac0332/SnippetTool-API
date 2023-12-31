package br.com.usp.mac0332.snippettool.model;

import static jakarta.persistence.CascadeType.REFRESH;
import static jakarta.persistence.GenerationType.IDENTITY;

import java.util.Set;

import br.com.usp.mac0332.snippettool.dto.tag.TagCreateDto;
import br.com.usp.mac0332.snippettool.dto.tag.TagUpdateDto;
import br.com.usp.mac0332.snippettool.enums.Color;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tag", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;

	private String name;

	@Enumerated(EnumType.STRING)
	private Color color;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, REFRESH})
	@JoinTable(name = "tag_snippet", joinColumns = @JoinColumn(name = "tag_id"), inverseJoinColumns = @JoinColumn(name = "snippet_id"))
	private Set<Snippet> snippets;

	public Tag(TagCreateDto tagDto, User user) {
		this.name = tagDto.name();
		this.color = Color.valueOf(tagDto.color());
		this.user = user;
	}
	
	public void addSnippet(Snippet snippet) {
		this.snippets.add(snippet);
	}
	
	public void removeSnippet(Snippet snippet) {
		this.snippets.remove(snippet);
	}

	public Tag update(TagUpdateDto updatedTag) {
		if (StringUtils.isNotBlank(updatedTag.name())) {
			this.setName(updatedTag.name());
		}
		if (StringUtils.isNotBlank(updatedTag.color())) {
			this.setColor(Color.valueOf(updatedTag.color()));
		}
		return this;
	}

}
