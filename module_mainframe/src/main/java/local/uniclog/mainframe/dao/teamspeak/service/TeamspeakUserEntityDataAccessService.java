package local.uniclog.mainframe.dao.teamspeak.service;

import local.uniclog.mainframe.dao.teamspeak.dto.TeamspeakUserEntityDataTransferObject;

import java.util.List;

/**
 * Data Access Service for TeamspeakUserEntity
 *
 * @author uniclog
 * @version 0.1
 */
public interface TeamspeakUserEntityDataAccessService {
    /**
     * Обертка для сохранения сущности в бд
     *
     * @param object user {@link Object}
     * @param <T>    объект для метода save
     * @return {@link TeamspeakUserEntityDataTransferObject}
     */
    <T> TeamspeakUserEntityDataTransferObject save(T object);

    /**
     * Обертка для обновления полей записи
     *
     * @param object {@link Object}
     * @param <T>    объект для метода update
     * @return {@link TeamspeakUserEntityDataTransferObject}
     */
    <T> TeamspeakUserEntityDataTransferObject update(T object);

    /**
     * Обертка для получения пользователя по его token
     *
     * @param token teamspeak-token
     * @return {@link TeamspeakUserEntityDataTransferObject}
     */
    TeamspeakUserEntityDataTransferObject findByTeamspeakToken(String token);

    /**
     * Обертка для получения всх записей с флагом subscribe
     *
     * @return List&lt;{@link TeamspeakUserEntityDataTransferObject}&gt;
     */
    List<TeamspeakUserEntityDataTransferObject> findAllSubscribers();

    /**
     * Обертка для получения всех записей из бд
     *
     * @return List&lt;{@link TeamspeakUserEntityDataTransferObject}&gt;
     */
    List<TeamspeakUserEntityDataTransferObject> findAll();

    /**
     * Обертка для удаления записи по token
     *
     * @param token teamspeak-token
     * @return List&lt;{@link TeamspeakUserEntityDataTransferObject}&gt;
     */
    List<TeamspeakUserEntityDataTransferObject> deleteByTeamspeakToken(String token);
}
