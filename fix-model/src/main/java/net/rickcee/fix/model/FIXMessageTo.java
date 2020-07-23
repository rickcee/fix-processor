/**
 * 
 */
package net.rickcee.fix.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author rickcee
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "FIX_MSG_TO")
public class FIXMessageTo extends AuditableEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	@Column
	private String entity;
	@Column
	private String source;
	@Column
	@Lob
	private String msgContent;

}
