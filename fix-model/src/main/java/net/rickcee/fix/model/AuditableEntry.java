/**
 * 
 */
package net.rickcee.fix.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import lombok.Data;

/**
 * @author rickcee
 *
 */
@Data
@MappedSuperclass
public class AuditableEntry {

	@Column
	private String modifiedBy;
	@Column
	private Date modifiedOn;
	@Column
	private String modifiedByServer;

	@PrePersist
	private void preInsertUpdate() {
		if (this.modifiedBy == null) {
			this.modifiedBy = Constants.USERNAME;
		}
		this.modifiedByServer = Constants.HOSTNAME;
		this.modifiedOn = new Date();
	}
}
