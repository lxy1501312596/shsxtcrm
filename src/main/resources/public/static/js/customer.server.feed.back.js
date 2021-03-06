function searchCustomerServeByParams() {
    $("#dg").datagrid("load",{
        customer:$("#s_customer").val(),
        type:$("#s_serveType").combobox("getValue")
    })
}


function openFeedBackDialog() {
    var rows=$("#dg").datagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("来自crm","请选择待分配的数据!","error");
        return;
    }
    if(rows.length>1){
        $.messager.alert("来自crm","暂不支持批量分配!","error");
        return;
    }

    $("#fm").form("load",rows[0]);
    openDialog("dlg","服务处理");
}

function doFeedBack() {
    $.ajax({
        type:"post",
        url:ctx+"/customer_serve/save",
        data:{
            state:"fw_004",
            id:$("input[name='id']").val(),
            serviceProceResult:$("#serviceProceResult").val(),
            myd:$("#myd").combobox("getValue"),
        },
        dataType:"json",
        success:function (data) {
            if(data.code==200){
                closeDialog("dlg");
                searchCustomerServeByParams();
            }
        }
    })
}