package com.study.springboot.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
//JPA Entity 클래스들이 해당 클래스를 상속할 경우 필드들도 칼럼으로 인식하도록 함.
@MappedSuperclass
//Auditing 기
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    //엔티티가 생성되어 저장될 때의 시간 자동 저장.
    @CreatedDate
    private LocalDateTime createDate;

    //조회한 엔티티의 값을 변경할 때 시간이 자동 저장.
    @LastModifiedDate
    private LocalDateTime modifiedDate;

}
