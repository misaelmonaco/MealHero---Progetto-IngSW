package it.unisannio.menuservice.repository;

import it.unisannio.menuservice.entity.MenuItem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemRepository extends CrudRepository<MenuItem, Integer> {

    @Query("SELECT mi FROM MenuItem mi WHERE mi.menuId = :menuId")
    Iterable<MenuItem> findByMenuIdId(Integer menuId);

    @Modifying
    @Query("DELETE FROM MenuItem mi WHERE mi.menuId = :menuId")
    void deleteByMenuId(@Param("menuId") int menuId);
}
