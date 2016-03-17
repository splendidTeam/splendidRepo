--require "logging.file"
--local logger = logging.file("/home/appuser/webroot/lua.log", "%Y-%m-%d")
--logger:setLevel(logging.DEBUG)

background = "red";
image_size = {"100x20","200x80"};
image_ext = {"jpg","png"};

function string.split(str, delim, maxNb)   
    -- Eliminate bad cases...   
    if string.find(str, delim) == nil then  
        return { str }  
    end  
    if maxNb == nil or maxNb < 1 then  
        maxNb = 0    -- No limit   
    end  
    local result = {}  
    local pat = "(.-)" .. delim .. "()"   
    local nb = 0  
    local lastPos   
    for part, pos in string.gfind(str, pat) do  
        nb = nb + 1  
        result[nb] = part   
        lastPos = pos   
        if nb == maxNb then break end  
    end  
    -- Handle the last field   
    if nb ~= maxNb then  
        result[nb + 1] = string.sub(str, lastPos)   
    end  
    return result   
end  

function table.contains(table, element)
    for _, value in pairs(table) do
        if value == element then
            return true
        end
    end
    return false
end

function resize()
    local index = string.find(ngx.var.uri, "_%d*x%d*_%d+%.%a+")
    local originalUri = string.sub(ngx.var.uri, 0, index-1)
    local area = string.sub(ngx.var.uri, index+1)
    local ary = string.split(area, "_", 0)
    local dimensions = ary[1]
    local tmp = ary[2]
    local tmp_ary = string.split(tmp, "%.", 0)
    local mode = tmp_ary[1]
    local ext = tmp_ary[2]
    local command
               
    if mode == "1" then
        command = "/usr/local/graphicsmagick/bin/gm convert " .. ngx.var.image_root .. originalUri .. " -thumbnail '" .. dimensions .. "!' +profile '*' " .. ngx.var.file
        --command = "/usr/local/graphicsmagick/bin/gm convert " .. ngx.var.image_root .. originalUri .. " -resize '" .. dimensions .. "!' +profile '*' " .. ngx.var.file
        --command = "/usr/local/graphicsmagick/bin/gm convert " .. ngx.var.image_root .. originalUri .. " -scale '" .. dimensions .. "!' +profile '*' " .. ngx.var.file
    elseif mode == "2" then
        command = "/usr/local/graphicsmagick/bin/gm convert " .. ngx.var.image_root .. originalUri .. " -thumbnail '" .. dimensions .. "' +profile '*' " .. ngx.var.file
        --command = "/usr/local/graphicsmagick/bin/gm convert " .. ngx.var.image_root .. originalUri .. " -resize '" .. dimensions .. "' +profile '*' " .. ngx.var.file
        --command = "/usr/local/graphicsmagick/bin/gm convert " .. ngx.var.image_root .. originalUri .. " -scale '" .. dimensions .. "' +profile '*' " .. ngx.var.file
    elseif mode == "3" then
        command = "/usr/local/graphicsmagick/bin/gm convert " .. ngx.var.image_root .. originalUri .. " -thumbnail '" .. dimensions .. "^' +profile '*' " .. ngx.var.file
        --command = "/usr/local/graphicsmagick/bin/gm convert " .. ngx.var.image_root .. originalUri .. " -resize '" .. dimensions .. "^' +profile '*' " .. ngx.var.file
        --command = "/usr/local/graphicsmagick/bin/gm convert " .. ngx.var.image_root .. originalUri .. " -scale '" .. dimensions .. "^' +profile '*' " .. ngx.var.file
    elseif mode == "4" then
        command = "/usr/local/graphicsmagick/bin/gm convert " .. ngx.var.image_root .. originalUri .. " -thumbnail '" .. dimensions .. "^' +profile '*' -gravity center -extent " .. dimensions .. " " .. ngx.var.file
        --command = "/usr/local/graphicsmagick/bin/gm convert " .. ngx.var.image_root .. originalUri .. " -resize '" .. dimensions .. "^' +profile '*' -gravity center -extent " .. dimensions .. " " .. ngx.var.file
        --command = "/usr/local/graphicsmagick/bin/gm convert " .. ngx.var.image_root .. originalUri .. " -scale '" .. dimensions .. "^' +profile '*' -gravity center -extent " .. dimensions .. " " .. ngx.var.file
    elseif mode == "5" then
        if ext == "png" then
            command = "/usr/local/graphicsmagick/bin/gm convert " .. ngx.var.image_root .. originalUri .. " -thumbnail '" .. dimensions .. "' +profile '*' -background transparent -gravity center -extent " .. dimensions .. " " .. ngx.var.file
            --command = "/usr/local/graphicsmagick/bin/gm convert " .. ngx.var.image_root .. originalUri .. " -resize '" .. dimensions .. "' +profile '*' -background transparent -gravity center -extent " .. dimensions .. " " .. ngx.var.file
            --command = "/usr/local/graphicsmagick/bin/gm convert " .. ngx.var.image_root .. originalUri .. " -scale '" .. dimensions .. "' +profile '*' -background transparent -gravity center -extent " .. dimensions .. " " .. ngx.var.file
        else
            command = "/usr/local/graphicsmagick/bin/gm convert " .. ngx.var.image_root .. originalUri .. " -thumbnail '" .. dimensions .. "' +profile '*' -background '" .. background .. "' -gravity center -extent " .. dimensions .. " " .. ngx.var.file
            --command = "/usr/local/graphicsmagick/bin/gm convert " .. ngx.var.image_root .. originalUri .. " -resize '" .. dimensions .. "' +profile '*' -background '" .. background .. "' -gravity center -extent " .. dimensions .. " " .. ngx.var.file
            --command = "/usr/local/graphicsmagick/bin/gm convert " .. ngx.var.image_root .. originalUri .. " -scale '" .. dimensions .. "' +profile '*' -background '" .. background .. "' -gravity center -extent " .. dimensions .. " " .. ngx.var.file
        end
    else 
        ngx.exit(404)
    end

--    logger:debug("command=%s",command)

    if table.contains(image_size, dimensions) and table.contains(image_ext, ext) then
        os.execute(command)
    else
        ngx.exit(404)
    end
end

local status,error = pcall(resize)
--logger:debug("status=%s", status)
--logger:debug("error=%s", error)

