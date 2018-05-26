syntax enable " enable syntax highlighting
set tabstop=3 " number of visual spaces per tab
set softtabstop=3 " number of spaces in tab when editing
" set expandtab " tabs are spaces
set number " show line numbers
set showcmd " show command in bottom bar
" set cursorline " show cursor line
set wildmenu "visual autocomplete for command menu
set showmatch " enables highlighting of matching brackets
set incsearch " search as characters are entered
set hlsearch " highlight matches
" set paste " and nopaste - allows pasting without comments being redone
set textwidth=0 " column width (default 80) (0 means none)
set nowrap " turn off line wrapping

" turn off the search highlighting (mapping it to a \space)
nnoremap <leader><space> :nohlsearch<CR>

"set foldenable "enable folding
"set foldlevelstart=10
"set foldnestmax=10
"nnoremap <space> za "space open/close folds
"set foldmethod=indent " fold based on indent level

" change the leader key (default is \)
let mapleader=","

" set default file type
set filetype=conf

" set txt files to conf to get the syntax highlighting
autocmd BufEnter * if &filetype == "text" | setlocal filetype=conf | endif

" set colors for matching brackets
let g:loaded_matchparen=1

" :help cterm-colors
"hi TabLineFill term=NONE cterm=NONE ctermbg=NONE guibg=NONE
"hi clear

":hi TabLineFill guifg=none ctermfg=none guibg=none ctermbg=none gui=none cterm=none
":hi TabLine guifg=none ctermfg=none guibg=none ctermbg=none gui=none cterm=none
":hi TabLineSel guifg=none ctermfg=none guibg=none ctermbg=none gui=none cterm=inverse
