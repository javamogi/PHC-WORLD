package com.phcworld.repository.message;

import com.phcworld.domain.message.*;
import com.phcworld.domain.message.dto.ChatRoomSelectDto;
import com.phcworld.domain.user.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javafx.beans.binding.StringExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryCustomImpl implements ChatRoomRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    QChatRoom chatRoom = QChatRoom.chatRoom;
    QChatRoomUser chatRoomUser = QChatRoomUser.chatRoomUser;
    QChatRoomMessage message = QChatRoomMessage.chatRoomMessage;

    @Override
    public List<ChatRoomSelectDto> findChatRoomListByUser(User user){
        Map<Long, ChatRoomSelectDto> map = queryFactory
                .from(chatRoom)
                .leftJoin(chatRoom.users, chatRoomUser)
                .leftJoin(message).on(message.id.eq(
                        JPAExpressions
                                .select(message.id.max())
                                .from(message)
                                .where(message.chatRoom.eq(chatRoom))
                ))
                .where(chatRoom.users.contains(
                        JPAExpressions
                                .select(chatRoomUser)
                                .from(chatRoomUser)
                                .where(chatRoomUser.chatRoom.eq(chatRoom)
                                        .and(chatRoomUser.user.eq(user)))
                ))
                .orderBy(message.isRead.asc(), message.sendDate.desc())
                .transform(groupBy(chatRoom.id).as(Projections.fields(ChatRoomSelectDto.class,
                        chatRoom.id.as("chatRoomId"),
                        message.message.as("lastMessage"),
                        message.isRead,
                        list(
                                Expressions.stringTemplate("{0}", chatRoomUser.user.name)
                        ).as("users"),
                        message.sendDate.as("date")
                )));
        return new ArrayList<>(map.values());
    }

    @Override
    public ChatRoom findByChatRoomByUsers(List<Long> ids){
        return queryFactory
                .select(chatRoom)
                .from(chatRoom)
                .where(eqUsers(ids))
                .fetchOne();
    }

    private BooleanBuilder eqUsers(List<Long> ids) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (ids.size() == 0) {
            booleanBuilder = null;
        } else {
            for (Long id : ids) {
                booleanBuilder.and(chatRoom.users.any().user.id.eq(id));
            }
        }
        return booleanBuilder;
    }
}
