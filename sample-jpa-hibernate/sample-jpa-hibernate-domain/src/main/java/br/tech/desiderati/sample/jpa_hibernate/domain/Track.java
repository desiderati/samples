/*
 * Copyright (c) 2025 - Felipe Desiderati
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package br.tech.desiderati.sample.jpa_hibernate.domain;

import dev.springbloom.data.jpa.AbstractPersistableIdentity;
import dev.springbloom.jms.AsyncMessage;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "track", indexes = {
    @Index(columnList = "ID", name = "pk_track", unique = true)
})
@SuppressWarnings("JpaDataSourceORMInspection")
public class Track extends AbstractPersistableIdentity<Long> implements AsyncMessage {

    @Transient
    private final UUID msgId = UUID.randomUUID();

    @Column(name = "author", nullable = false)
    @NotBlank(message = "{track.author.notBlank}")
    private String author;

    @Column(name = "track_name", nullable = false)
    @NotBlank(message = "{track.trackName.notBlank}")
    private String trackName;

    /**
     * In Seconds
     */
    @Column(name = "duration", nullable = false)
    @Min(value = 10, message = "{track.duration.minValue}")
    @NotNull(message = "{track.duration.notNull}")
    private Integer duration;

}
