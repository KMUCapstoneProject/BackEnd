package kr.ac.kmu.Capstone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "category")
public class Category {

    // DB에서 직접 추가
    // {0 전부 다 - 디비에서는 생략}, 1 비교과 , 2 행사

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="category_id")
    private Long categoryId;

    @Column(nullable = false, length = 30)
    private String name; // 카테고리명

}
