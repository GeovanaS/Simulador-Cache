# Simulador-Cache
Trabalho AOC2 - 20162

Implementação do Simulador

Deverá ser implementado um simulador funcional de caches com dois níveis de cache (L1 e L2), sendo a
cache L1 splited (caches separadas para dados e instruções) e a cache L2 unified (uma cache única para
dados e instruções), em uma linguagem de programação qualquer e o simulador deverá ser parametrizável
(configurações da cache). A cache é endereçada a word e endereço possui 32 bits.
A configuração de cache deverá ser repassada por linha de comando é formatada com os seguintes
parâmetros (o arquivo de entrada poderá ter extensão):

cache_simulator <nsets_L1i> <bsize_L1i> <assoc_L1i> <nsets_L1d> <bsize_L1d> <assoc_L1d>
<nsets_L2>:<bsize_L2>:<assoc_L2> arquivo_de_entrada


Onde cada um destes campos possui o seguinte significado:

 <nsets> número de conjuntos na cache;


 <bsize> tamanho do bloco em bytes;


 <assoc> associatividade;


A política de substituição será sempre randômica. Já a política de escrita deverá ser write-back. A
configuração default será de uma cache com mapeamento direto com tamanho de bloco de 4 bytes e com
1024 conjuntos (nas duas caches). O tamanho da cache é dado pelo produto do número de conjuntos na
cache (<nsets>), tamanho do bloco em bytes (<bsize>) e associatividade (<assoc>).

A saída do simulador será um relatório de estatísticas com o número total de acessos, número total hits e
misses (os misses deverão ser classificados em compulsórios, capacidade e conflito), além do hit ratio e
miss ratio separados por nível (local miss rate) e também global miss rate. Além disso, deve ter a
estatística de número escritas (write) e leituras (read). O programa também deve fornecer o total de write
miss por cache.

O arquivo de entrada indica a leitura de um arquivo que será utilizado como entrada para o simulador
(armazenado em formato binário) que conterá os endereços requisitados a cache (endereços em 32 bits) –
seguido de um valor indicando a leitura ou escrita (leitura =0 e escrita =1) e endereços abaixo de XX
serão considerados acessos a memória de dados.
