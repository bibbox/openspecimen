
angular.module('os.biospecimen.cp.list', ['os.biospecimen.models'])
  .controller('CpListCtrl', function(
    $scope, $state, cpsCtx, CollectionProtocol, DeleteUtil, AuthorizationService) {

    var ctx;

    function init() {
      $scope.allowReadJobs = AuthorizationService.isAllowed($scope.participantResource.createOpts) ||
        AuthorizationService.isAllowed($scope.participantResource.updateOpts) ||
        AuthorizationService.isAllowed($scope.specimenResource.updateOpts);

      ctx = $scope.ctx = {
        params: {
          listName: 'cp-list-view',
          objectId: -1
        },

        emptyState: {
          loadingMessage: 'cp.loading_list',
          emptyMessage: 'cp.empty_list'
        }
      };
    }

    function getCpIds(cps) {
      return cps.map(function(cp) { return +cp.hidden.cpId; });
    }

    $scope.showCpSummary = function(row) {
      $state.go('cp-summary-view', {cpId: row.hidden.cpId});
    };

    $scope.deleteCps = function() {
      var cps = ctx.listCtrl.checkList.getSelectedItems();

      var opts = {
        confirmDelete:  'cp.delete_cps',
        successMessage: 'cp.cps_deleted',
        pendingMessage: 'cp.cps_delete_pending',
        onBulkDeletion: function() { ctx.listCtrl.loadList(); },
        askReason:      true
      }

      DeleteUtil.bulkDelete({bulkDelete: CollectionProtocol.bulkDelete}, getCpIds(cps), opts);
    }

    $scope.setListCtrl = function(listCtrl) {
      ctx.listCtrl = listCtrl;
      ctx.showSearch = listCtrl.haveFilters;
      listCtrl.viewCtx = cpsCtx;
    }

    $scope.toggleStarredCp = function(cp) {
      var q;
      if (cp.$$starred) {
        q = new CollectionProtocol({id: +cp.hidden.cpId}).unstar();
      } else {
        q = new CollectionProtocol({id: +cp.hidden.cpId}).star();
      }

      q.then(
        function(result) {
          if (result.status == true) {
            cp.$$starred = !cp.$$starred;
          }
        }
      );
    }

    init();
  });
