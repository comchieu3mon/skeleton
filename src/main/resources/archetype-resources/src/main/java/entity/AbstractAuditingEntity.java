#set($symbol_pound='#')
#set($symbol_dollar='$')
#set($symbol_escape='\' )
package ${package}.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Base abstract class for entities which will hold definitions for created, last modified, created
 * by, last modified by attributes.
 *
 * @author duccaom
 * @version 1.0
 * @since 2022/05/13
 */
@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditEntityEventListener.class)
public class AbstractAuditingEntity implements Serializable {

  private static final long serialVersionUID = 1L;


  @Column(name = "created_by")
  @JsonIgnore
  @NotNull
  private Long createdBy;

  @Column(name = "created_when")
  @JsonIgnore
  @NotNull
  private LocalDateTime createdWhen;

  @Column(name = "modified_by")
  @JsonIgnore
  @NotNull
  private Long modifiedBy;

  @Column(name = "modified_when")
  @JsonIgnore
  @NotNull
  private LocalDateTime modifiedWhen;
}
