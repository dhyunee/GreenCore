package com.chicochico.domain.user.entity;


import com.chicochico.common.code.IsDeletedType;
import com.chicochico.common.entity.CommonEntity;
import com.chicochico.domain.plant.entity.PlantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_plant")
public class UserPlantEntity extends CommonEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false) // FK 이름 지정
	private UserEntity user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "plant_id", nullable = false) // FK 이름 지정
	private PlantEntity plant;

	@Column(nullable = false)
	private String plantNickname;

	@Column(nullable = false, length = 500)
	private String plantImagePath;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private IsDeletedType isDeleted;


	public void setPlantNickname(String plantNickname) {
		this.plantNickname = plantNickname;
	}


	public void setIsDeleted(IsDeletedType isDeleted) {
		this.isDeleted = isDeleted;
	}

}
