$(document).ready(function () {

    // ================= FILTRO TABELLA OPERATORI =================
    if ($('#operatorsTable').length) {

        var operatorsTable = $('#operatorsTable').DataTable();

        $('#statusFilter').on('change', function () {
            var status = $(this).val();

            operatorsTable
                .column(3)
                .search(status)
                .draw();
        });
    }


    // ================= FILTRO TABELLA RICHIESTE =================
    if ($('#requestsTable').length) {

        var requestsTable = $('#requestsTable').DataTable();

        $('#faseFilter').on('change', function () {
            var fase = $(this).val();

            if (fase === '') {
                requestsTable
                    .column(4)
                    .search('')
                    .draw();
            } else {
                requestsTable
                    .column(4)
                    .search('^' + $.fn.dataTable.util.escapeRegex(fase) + '$', true, false)
                    .draw();
            }
        });
    }

});