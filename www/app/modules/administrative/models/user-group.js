angular.module('os.administrative.models.user')
  .factory('UserGroup', function(osModel, $http) {
    var UserGroup = osModel('user-groups');

    UserGroup.prototype.getDisplayName = function() {
      return this.name;
    }

    UserGroup.prototype.getType = function() {
      return 'user_group';
    }

    UserGroup.prototype.addUsers = function(users) {
      return $http.put(UserGroup.url() + this.$id() + '/users?op=ADD', users).then(
        function(resp) {
          return new UserGroup(resp.data);
        }
      );
    }

    UserGroup.prototype.removeUsers = function(users) {
      return $http.put(UserGroup.url() + this.$id() + '/users?op=REMOVE', users).then(
        function(resp) {
          return new UserGroup(resp.data);
        }
      );
    }

    return UserGroup;
  });
