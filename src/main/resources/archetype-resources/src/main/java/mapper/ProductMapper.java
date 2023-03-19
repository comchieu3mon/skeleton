#set($symbol_pound='#')
#set($symbol_dollar='$')
#set($symbol_escape='\' )
package ${package}.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * The Mapper class helps to map data related to Product business.
 *
 * @author duccaom
 * @version 1.0
 * @since 2022/09/20
 */
@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    imports = {LocalDateTime.class},
    uses = {StatusMapper.class})
public interface ProductMapper {

}
