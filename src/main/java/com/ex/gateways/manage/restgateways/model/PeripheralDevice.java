package com.ex.gateways.manage.restgateways.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "peripheral")
public class PeripheralDevice {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uid")
    private Long uid;

    @Column(name = "vendor")
    private String vendor;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gateway_id", nullable = false)
    @EqualsAndHashCode.Exclude
    private Gateway gateway;
}
