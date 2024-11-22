document$.subscribe(async () => {
    const url = `https://api.allorigins.win/raw?url=${encodeURIComponent('https://andre601.ch/oneversionremake/protocol_versions.json')}`;
    
    const versions_table = document.querySelector('[data-md-component="versions-table"]');
    
    const headerText = ['Protocol', 'Version', 'Major'];
    
    function createTable(data) {
        const table = document.createElement("table");
        const tableHead = document.createElement("thead");
        const tableBody = document.createElement("tbody");
        
        if(tableHead === null || tableBody === null)
            return;
        
        const headRow = document.createElement("tr");
        for(let i = 0; i < 3; i++) {
            const cell = document.createElement("th");
            const text = document.createTextNode(`${headerText[`${i}`]}`);
            
            cell.appendChild(text);
            headRow.appendChild(cell);
        }
        
        tableHead.appendChild(headRow);
        
        console.log(data.protocols)
        
        for(i in data.protocols){
            const value = data.protocols[i];
            const row = document.createElement("tr");
            
            const protocol = document.createElement("td");
            const protocolText = document.createTextNode(value.protocol)
            
            const version = document.createElement("td");
            const versionText = document.createTextNode(value.name)
            
            const major = document.createElement("td");
            const majorText = document.createTextNode(value.major)
            
            protocol.appendChild(protocolText);
            version.appendChild(versionText);
            major.appendChild(majorText);
            
            row.appendChild(protocol);
            row.appendChild(version);
            row.appendChild(major);
            
            tableBody.appendChild(row);
        }
        
        table.appendChild(tableHead);
        table.appendChild(tableBody);
        
        versions_table.appendChild(table);
    }
    
    async function fetchData() {
        const data = await fetch(`${url}`).then(_ => _.json());
        
        __md_set("__versions_data", data, sessionStorage);
        createTable(data);
    }
    
    if(document.querySelector('[data-md-component="versions-table"]')) {
        const cached = __md_get("__versions_data", sessionStorage);
        if(cached != null) {
            createTable(cached);
        } else {
            fetchData();
        }
    }
})