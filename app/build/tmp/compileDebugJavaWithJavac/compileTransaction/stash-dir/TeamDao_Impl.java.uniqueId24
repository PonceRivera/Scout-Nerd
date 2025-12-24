package com.scoutnerd.app.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.scoutnerd.app.data.local.entity.EventTeamJoin;
import com.scoutnerd.app.data.local.entity.TeamEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TeamDao_Impl implements TeamDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TeamEntity> __insertionAdapterOfTeamEntity;

  private final EntityInsertionAdapter<EventTeamJoin> __insertionAdapterOfEventTeamJoin;

  public TeamDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTeamEntity = new EntityInsertionAdapter<TeamEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `teams` (`key`,`teamNumber`,`nickname`,`name`,`city`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final TeamEntity entity) {
        if (entity.key == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.key);
        }
        statement.bindLong(2, entity.teamNumber);
        if (entity.nickname == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.nickname);
        }
        if (entity.name == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.name);
        }
        if (entity.city == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.city);
        }
      }
    };
    this.__insertionAdapterOfEventTeamJoin = new EntityInsertionAdapter<EventTeamJoin>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `event_teams` (`eventKey`,`teamKey`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final EventTeamJoin entity) {
        if (entity.eventKey == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.eventKey);
        }
        if (entity.teamKey == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.teamKey);
        }
      }
    };
  }

  @Override
  public void insertTeams(final List<TeamEntity> teams) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTeamEntity.insert(teams);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertEventTeamJoins(final List<EventTeamJoin> joins) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfEventTeamJoin.insert(joins);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<TeamEntity>> getTeamsForEvent(final String eventKey) {
    final String _sql = "SELECT * FROM teams INNER JOIN event_teams ON teams.`key` = event_teams.teamKey WHERE event_teams.eventKey = ? ORDER BY teams.teamNumber ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (eventKey == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, eventKey);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"teams",
        "event_teams"}, true, new Callable<List<TeamEntity>>() {
      @Override
      @Nullable
      public List<TeamEntity> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfKey = CursorUtil.getColumnIndexOrThrow(_cursor, "key");
            final int _cursorIndexOfTeamNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "teamNumber");
            final int _cursorIndexOfNickname = CursorUtil.getColumnIndexOrThrow(_cursor, "nickname");
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
            final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
            final List<TeamEntity> _result = new ArrayList<TeamEntity>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final TeamEntity _item;
              final String _tmpKey;
              if (_cursor.isNull(_cursorIndexOfKey)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getString(_cursorIndexOfKey);
              }
              final int _tmpTeamNumber;
              _tmpTeamNumber = _cursor.getInt(_cursorIndexOfTeamNumber);
              final String _tmpNickname;
              if (_cursor.isNull(_cursorIndexOfNickname)) {
                _tmpNickname = null;
              } else {
                _tmpNickname = _cursor.getString(_cursorIndexOfNickname);
              }
              final String _tmpName;
              if (_cursor.isNull(_cursorIndexOfName)) {
                _tmpName = null;
              } else {
                _tmpName = _cursor.getString(_cursorIndexOfName);
              }
              final String _tmpCity;
              if (_cursor.isNull(_cursorIndexOfCity)) {
                _tmpCity = null;
              } else {
                _tmpCity = _cursor.getString(_cursorIndexOfCity);
              }
              _item = new TeamEntity(_tmpKey,_tmpTeamNumber,_tmpNickname,_tmpName,_tmpCity);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
