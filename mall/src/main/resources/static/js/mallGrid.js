
/**
* 지원 그리드 라이브러리
* simple : simple-datatables (서버 사이드 페이지네이션 지원하지 않음)
*          무조건 한번에 데이터를 다 불러와야 한다.
* jqGrid : jqGrid
* gridJs : Grid.js
*/
class MallGrid {
    constructor(type, options) {
        this.type = type;
        this.grid = null;

        if (type === 'simple') {
            this.grid = new simpleDatatables.DataTable('#' + options.id, {
                searchable: false //검색 박스 제거
            });
        }
        else if (type === 'jqGrid') {
            this.grid = $('#' + options.id);
            this.grid.jqGrid(options);
        }
    }

    getGrid() {
        return this.grid;
    }

    addRows(newRows) {
        if (!newRows) return;
        for(let i = 0; i < newRows.length; i++) {
            this.addRow(newRows[i]);
        }
    }
    addRow(newRow) {
        if (this.type === 'simple') {
            // 배열로 입력해야 하며 컬럼 순서대로 row 추가
            this.grid.rows.add(Object.values(newRow));
        } else if (this.type === 'jqGrid') {
            this.grid.jqGrid('addRowData', newRow.id, newRow);
        }
    }

    addColumns(newColumns) {
        if (!newColumns) return;
        for(let i = 0; i < newColumns.length; i++) {
            this.addColumn(newColumns[i]);
        }
    }
    addColumn(newColumn) {
        if (this.type === 'simple') {
            this.grid.columns.add({
                heading: newColumn.label,
            });
        }
    }

    setPageInfo(totalPages, currentPage, totalCount) {
        if (this.type === 'simple') {
            this.grid.options.labels.info = `총 ${totalCount} 건`;
            this.grid.update();

            this.grid._currentPage = currentPage;
            this.grid.totalPages = totalPages;
            this.grid._renderPagers();
        }
    }
    getPerPage() {
        return this.grid.options.perPage;
    }


}