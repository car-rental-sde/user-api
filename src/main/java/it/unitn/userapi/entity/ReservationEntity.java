package it.unitn.userapi.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "reservation")
public class ReservationEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@Column(nullable = false)
	private Long carId;

	@Column(nullable = false)
	private LocalDate beginDate;

	@Column(nullable = false)
	private LocalDate endDate;

	@Column(nullable = false)
	private String beginPlace;

	@Column(nullable = false)
	private String endPlace;

	@Column(nullable = false)
	private String beginPosition;

	@Column(nullable = false)
	private String endPosition;

	@Column(nullable = false)
	private Boolean isMaintenance;

	@Column()
	private String details;

	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id", nullable=false)
	private UserEntity user;
}
